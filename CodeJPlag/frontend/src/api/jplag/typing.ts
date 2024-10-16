interface JPlagByFilesOption {
    language: 'Java' | 'JavaScript' | 'TypeScript' | 'C',
    files: File[],
    baseCode: File | null | undefined
}